(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('matiere', {
            parent: 'entity',
            url: '/matiere?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Matieres'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/matiere/matieres.html',
                    controller: 'MatiereController',
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
        .state('matiere-detail', {
            parent: 'entity',
            url: '/matiere/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Matiere'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/matiere/matiere-detail.html',
                    controller: 'MatiereDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Matiere', function($stateParams, Matiere) {
                    return Matiere.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'matiere',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('matiere-detail.edit', {
            parent: 'matiere-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere/matiere-dialog.html',
                    controller: 'MatiereDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Matiere', function(Matiere) {
                            return Matiere.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('matiere.new', {
            parent: 'matiere',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere/matiere-dialog.html',
                    controller: 'MatiereDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('matiere', null, { reload: 'matiere' });
                }, function() {
                    $state.go('matiere');
                });
            }]
        })
        .state('matiere.edit', {
            parent: 'matiere',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere/matiere-dialog.html',
                    controller: 'MatiereDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Matiere', function(Matiere) {
                            return Matiere.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('matiere', null, { reload: 'matiere' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('matiere.delete', {
            parent: 'matiere',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/matiere/matiere-delete-dialog.html',
                    controller: 'MatiereDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Matiere', function(Matiere) {
                            return Matiere.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('matiere', null, { reload: 'matiere' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
