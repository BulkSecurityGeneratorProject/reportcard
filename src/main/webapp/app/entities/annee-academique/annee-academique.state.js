(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('annee-academique', {
            parent: 'entity',
            url: '/annee-academique?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AnneeAcademiques'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/annee-academique/annee-academiques.html',
                    controller: 'AnneeAcademiqueController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('annee-academique-detail', {
            parent: 'entity',
            url: '/annee-academique/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AnneeAcademique'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/annee-academique/annee-academique-detail.html',
                    controller: 'AnneeAcademiqueDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AnneeAcademique', function($stateParams, AnneeAcademique) {
                    return AnneeAcademique.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'annee-academique',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('annee-academique-detail.edit', {
            parent: 'annee-academique-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/annee-academique/annee-academique-dialog.html',
                    controller: 'AnneeAcademiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AnneeAcademique', function(AnneeAcademique) {
                            return AnneeAcademique.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('annee-academique.new', {
            parent: 'annee-academique',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/annee-academique/annee-academique-dialog.html',
                    controller: 'AnneeAcademiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                anneedebut: null,
                                anneefin: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('annee-academique', null, { reload: 'annee-academique' });
                }, function() {
                    $state.go('annee-academique');
                });
            }]
        })
        .state('annee-academique.edit', {
            parent: 'annee-academique',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/annee-academique/annee-academique-dialog.html',
                    controller: 'AnneeAcademiqueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AnneeAcademique', function(AnneeAcademique) {
                            return AnneeAcademique.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('annee-academique', null, { reload: 'annee-academique' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('annee-academique.delete', {
            parent: 'annee-academique',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/annee-academique/annee-academique-delete-dialog.html',
                    controller: 'AnneeAcademiqueDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AnneeAcademique', function(AnneeAcademique) {
                            return AnneeAcademique.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('annee-academique', null, { reload: 'annee-academique' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
